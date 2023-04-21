package com.ew.communication.address.dto;

import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.entity.BaseEntity;
import com.ew.communication.address.entity.AddressBook;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <pre>
 *  参数实体映射
 * </pre>
 *
 * @author LiZhengFan
 * @date 2023-04-21
 */
@Mapper(componentModel = "spring", imports = {BaseEntity.class})
public interface AddressBookParamMapper {

    /**
     * 新增参数转换为实体类
     *
     * @param addressBookAddParam 新增参数
     * @return AddressBook 实体类
     * @date 2023-04-21
     */
    AddressBook addParam2Entity(AddressBookAddParam addressBookAddParam);

    /**
     * 修改参数转换为实体类
     *
     * @param addressBookEditParam 修改参数
     * @return AddressBook 实体类
     * @date 2023-04-21
     */
    AddressBook editParam2Entity(AddressBookEditParam addressBookEditParam);

    /**
     * 实体类换为Dto
     *
     * @param addressBook 实体类
     * @return AddressBookDto DTO
     * @date 2023-04-21
     */
    AddressBookDto entity2Dto(AddressBook addressBook);

    /**
     * 分页实体转DTO
     *
     * @param page 分页实体
     * @return PageResult<AddressBookDto> 分页DTO
     * @date 2023-04-21
     */
    PageResult<AddressBookDto> pageEntity2Dto(PageResult<AddressBook> page);


    /**
     * dto集合转entity集合
     *
     * @param rows dto列表
     * @return List<AddressBook> entity列表
     * @date 2023-04-21
     */
    List<AddressBook> dtoList2Entity(List<AddressBookDto> rows);

}
