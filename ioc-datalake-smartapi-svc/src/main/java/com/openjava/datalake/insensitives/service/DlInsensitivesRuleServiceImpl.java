package com.openjava.datalake.insensitives.service;

import com.openjava.datalake.common.AESkeyConstant;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.insensitives.domain.DlInsensitivesRule;
import com.openjava.datalake.insensitives.repository.DlInsensitivesRuleRepository;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.service.DlRescataStructurePermissionService;
import com.openjava.datalake.rescata.vo.TableData;
import com.openjava.datalake.util.AuditComponentUtils;
import com.openjava.datalake.util.EncryptUtils;
import com.openjava.datalake.util.ReflactUtils;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * 脱敏规则库业务层
 * @author xjd
 *
 */
@Service
@Transactional
public class DlInsensitivesRuleServiceImpl implements DlInsensitivesRuleService {

	private static Logger logger = LoggerFactory.getLogger(DlInsensitivesRuleServiceImpl.class);
	@Resource
	private DlRescataStructurePermissionService dlRescataStructurePermissionService;
	@Resource
	private DlInsensitivesRuleRepository dlInsensitivesRuleRepository;
	@Resource
	private SysCodeService sysCodeService;
	private static String SERVICE_NAME = "数据湖";
	private static String MODULE_NAME = "脱敏库管理";
	@Override
	public TableData encryptFilter(TableData tableData, String userAccount) {
		List<DlRescataColumn> dlRescataColumnList = tableData.getDlRescataColumnList();
		if (CollectionUtils.isEmpty(dlRescataColumnList)) {
			return null;
		}
		Map<Long, DlRescataStrucPermi> structurePermissionMap;//如果前面已经查过，不用重复去查了
		if(tableData.getStructurePermissions() != null){
			structurePermissionMap = tableData.getStructurePermissions().stream().collect(Collectors.toMap(DlRescataStrucPermi::getStructureId, Function.identity()));
		} else{
			structurePermissionMap = dlRescataStructurePermissionService.findAlreadyExistPermittedColumn(dlRescataColumnList, userAccount);
		}
		List<Object[]> data = tableData.getData();
		for (int columnNum = 0; columnNum < dlRescataColumnList.size(); columnNum++) {
			DlRescataColumn dlRescataColumn = dlRescataColumnList.get(columnNum);
			boolean needEncrypt = false;
			if (PublicConstant.YES.equals(dlRescataColumn.getIsEncrypt())) {
				DlRescataStrucPermi dlRescataStrucPermi = structurePermissionMap.get(dlRescataColumn.getStructureId());
				if (dlRescataStrucPermi != null) {
					Long isDecryption = dlRescataStrucPermi.getIsDecryption();
					needEncrypt = !PublicConstant.YES.equals(isDecryption);
				}
			}

			// 先判断这一列是否需要执行脱敏过滤，需要就每一行都执行，不需要就省了一个for
			if (needEncrypt) {
				// 有一个没有权限就是部分，因为上面已经判断了是否全部都没有，能下来的都是至少有一个权限的
				if (!PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE.equals(tableData.getPermissionLevel())) {
					tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_PART);
				}
				// rowNumber 是每一行
				for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
					Object[] objects = data.get(rowNumber);
					String sourceValue = String.valueOf(objects[columnNum]);
					String encryValue = EncryptUtils.aesEncry(AESkeyConstant.ivStr, AESkeyConstant.sKey, sourceValue);
					// 改变值
					objects[columnNum] = encryValue;
				}
			}

		}
		return null;
	}

	@Override
	public TableData inSensitivesFilter(TableData tableData, String userAccount) {
		List<DlRescataColumn> dlRescataColumnList = tableData.getDlRescataColumnList();
		List<Object[]> data = tableData.getData();
		if (CollectionUtils.isEmpty(dlRescataColumnList)) {
			return null;
		}
		// 不要用resourceCode，有可能会查出历史版本的字段
		/*Long resourceId = dlRescataColumnList.get(0).getResourceId();
		DlRescataResource byResourceId = dlRescataResourceService.findByResourceId(resourceId);
		String resourceCode = byResourceId.getResourceCode();
		List<DlRescataStrucPermi> structurePermissions = dlRescataStructurePermissionRepository.getResourcePermissionLevel(resourceCode, userAccount);
		*/
		Map<Long, DlRescataStrucPermi> structurePermissionMap;//如果前面已经查过，不用重复去查了
		if(tableData.getStructurePermissions() != null){
			structurePermissionMap = tableData.getStructurePermissions().stream().collect(Collectors.toMap(DlRescataStrucPermi::getStructureId, Function.identity()));
		} else{
			structurePermissionMap = dlRescataStructurePermissionService.findAlreadyExistPermittedColumn(dlRescataColumnList, userAccount);
		}
		// 先把所有的脱敏规则查询出来

		Map<Long, DlInsensitivesRule> insensitiveRuleMap = dlRescataColumnList.stream().filter(e -> Objects.nonNull(e.getInsensitivesRuleId()))
				.map(e -> this.get(e.getInsensitivesRuleId())).distinct().collect(Collectors.toMap(DlInsensitivesRule::getInsensitivesRuleId, Function.identity()));
		// columnNUmber 是每一列 （列序号）
		for (int columnNum = 0; columnNum < dlRescataColumnList.size(); columnNum++) {
			DlRescataColumn dlRescataColumn = dlRescataColumnList.get(columnNum);
			// 是否需要脱敏
			boolean needInsensitived = false;
			if (PublicConstant.YES.equals(dlRescataColumn.getIsDesensitization())) {
				DlRescataStrucPermi dlRescataStrucPermi = structurePermissionMap.get(dlRescataColumn.getStructureId());
				if (dlRescataStrucPermi != null) {
					Long isSensitived = dlRescataStrucPermi.getIsSensitived();
					// 没有解敏权限，需要脱敏 needInsensitived=true，ps:若有解敏权限则不需要脱敏needInsensitived=false
					needInsensitived = !PublicConstant.YES.equals(isSensitived);
				} else {
					// 没有权限，在上一步处理
					/*for (Object[] datum : data) {
						Object o = datum[columnNum];
						o = "没有权限";
					}*/
				}
			}
			// 先判断这一列是否需要执行脱敏过滤，需要就每一行都执行，不需要就省了一个for
			if (needInsensitived) {
				// 有一个没有权限就是部分，因为上面已经判断了是否全部都没有，能下来的都是至少有一个权限的
				if (!PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE.equals(tableData.getPermissionLevel())) {
					tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_PART);
				}
				Long insensitivesRuleId = dlRescataColumn.getInsensitivesRuleId();
				if (insensitivesRuleId == null) {
					for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
						Object[] objects = data.get(rowNumber);
						objects[columnNum] = "**脱敏字段设置有误**";
					}
					continue;
				}
				DlInsensitivesRule dlInsensitivesRule = insensitiveRuleMap.get(insensitivesRuleId);
				String insensitivesStartEnd = dlRescataColumn.getInsensitivesStartEnd();
				String[] split = null;
				if (StringUtils.isNotBlank(insensitivesStartEnd)) {
					try {
						split = insensitivesStartEnd.split(",");
						if (split == null || split.length != 2) {
							split = null;
							logger.error("脱敏规则字段没有按照规定填写，字段id[{}],字段名称[{}]，资源目录id[{}]", dlRescataColumn.getStructureId(), dlRescataColumn.getColumnName(), dlRescataColumn.getResourceId());
							for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
								Object[] objects = data.get(rowNumber);
								objects[columnNum] = "**脱敏字段设置有误**";
							}
							continue;
						}
						if ("0".equals(split[0])) {
							insensitivesStartEnd = split[1];
						}
					} catch (PatternSyntaxException e) {
						logger.error("脱敏规则字段没有按照规定填写，字段id[{}],字段名称[{}]，资源目录id[{}]", dlRescataColumn.getStructureId(), dlRescataColumn.getColumnName(), dlRescataColumn.getResourceId());
						for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
							Object[] objects = data.get(rowNumber);
							objects[columnNum] = "**脱敏字段设置有误**";
						}
					}
				}
				// rowNumber 是每一行
				for (int rowNumber = 0; rowNumber < data.size(); rowNumber++) {
					Object[] objects = data.get(rowNumber);
					String sourceValue = String.valueOf(objects[columnNum]);
					if (split != null) {
						sourceValue = StringUtils.join(sourceValue, ",", insensitivesStartEnd);
					}
					Object afterInsensitves = ReflactUtils.insensitiveColumn(dlInsensitivesRule, sourceValue);
					// 改变值
					objects[columnNum] = afterInsensitves;
				}
			}

		}
		return tableData;
	}

	@Override
	public DlInsensitivesRule get(Long id) {
		Optional<DlInsensitivesRule> o = dlInsensitivesRuleRepository.findById(id);
		if(o.isPresent()) {
			DlInsensitivesRule m = o.get();
			if(m.getRuleType() != null) {
				Map<String, SysCode> dlinsensitivesruletype = sysCodeService.getCodeMap("dl.insensitives.rule.type");
				SysCode c = dlinsensitivesruletype.get(m.getRuleType().toString());
				if(c != null) {
					m.setRuleTypeName(c.getCodename());
				}
			}
			if(m.getState() != null) {
				Map<String, SysCode> publicstate = sysCodeService.getCodeMap("public.state");
				SysCode c = publicstate.get(m.getState().toString());
				if(c != null) {
					m.setStateName(c.getCodename());
				}
			}
			AuditComponentUtils.genAudit(2L, SERVICE_NAME, MODULE_NAME, id.toString(), id, o, o.get().getRuleType() == 1 ? "脱敏字符集管理" : "脱敏规则库管理", "查看");
			return m;
		}
		System.out.println("找不到记录DlInsensitivesRule："+id);
		return null;
	}
}
