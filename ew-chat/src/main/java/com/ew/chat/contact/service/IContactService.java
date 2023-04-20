package com.ew.chat.contact.service;

import com.ew.chat.contact.entity.Contact;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.chat.contact.dto.ContactQueryParam;
import com.ew.chat.contact.dto.ContactAddParam;
import com.ew.chat.contact.dto.ContactEditParam;
import com.ew.chat.contact.dto.ContactDto;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-20
 */
public interface IContactService extends IBaseService<Contact> {

    /**
     * 分页查询，返回Dto
     *
     * @param contactQueryParam 查询参数
     * @return ContactDto 查询返回列表实体
     * @since 2023-04-20
     */
    PageResult<ContactDto> pageDto(ContactQueryParam contactQueryParam);

    /**
     * 列表查询，返回Dto
     *
     * @param contactQueryParam 查询参数
     * @return ContactDto 查询返回列表实体
     * @since 2023-04-20
     */
    List<ContactDto> listDto(ContactQueryParam contactQueryParam);

    /**
     * 新增
     *
     * @param contactAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-20
     */
    boolean saveByParam(ContactAddParam contactAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 联系人实体id
     * @return ContactDto
     * @since 2023-04-20
     */
    ContactDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-20
     */
    boolean saveDtoBatch(List<ContactDto> rows);

    /**
     * 更新
     *
     * @param contactEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-20
     */
    boolean updateByParam(ContactEditParam contactEditParam);
}