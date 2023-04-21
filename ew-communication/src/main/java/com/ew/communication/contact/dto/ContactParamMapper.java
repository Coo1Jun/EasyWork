package com.ew.communication.contact.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.contact.entity.Contact;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-20
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface ContactParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param contactAddParam 新增参数
     * @return Contact 实体类
     * @date 2023-04-20
     */
    Contact addParam2Entity(ContactAddParam contactAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param contactEditParam 修改参数
     * @return Contact 实体类
     * @date 2023-04-20
     */
    Contact editParam2Entity(ContactEditParam contactEditParam);

    /**
     * 实体类换为Dto
     *
     * @param contact 实体类
     * @return ContactDto DTO
     * @date 2023-04-20
     */
    ContactDto entity2Dto(Contact contact);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<ContactDto> 分页DTO
     * @date 2023-04-20
     */
    PageResult<ContactDto> pageEntity2Dto(PageResult<Contact> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<Contact> entity列表
     * @date 2023-04-20
     */
    List<Contact> dtoList2Entity(List<ContactDto> rows);

}
