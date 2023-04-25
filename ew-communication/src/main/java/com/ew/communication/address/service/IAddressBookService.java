package com.ew.communication.address.service;

import com.ew.communication.address.entity.AddressBook;
import cn.edu.hzu.common.service.IBaseService;
import com.ew.communication.address.dto.AddressBookQueryParam;
import com.ew.communication.address.dto.AddressBookAddParam;
import com.ew.communication.address.dto.AddressBookEditParam;
import com.ew.communication.address.dto.AddressBookDto;
import cn.edu.hzu.common.api.PageResult;

import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
public interface IAddressBookService extends IBaseService<AddressBook> {

    /**
     * 分页查询，返回Dto
     *
     * @param addressBookQueryParam 查询参数
     * @return AddressBookDto 查询返回列表实体
     * @since 2023-04-21
     */
    PageResult<AddressBookDto> pageDto(AddressBookQueryParam addressBookQueryParam);

    /**
     * 新增
     *
     * @param addressBookAddParam 新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean saveByParam(AddressBookAddParam addressBookAddParam);

    /**
     * 根据id查询，转dto
     *
     * @param id 通讯录信息实体id
     * @return AddressBookDto
     * @since 2023-04-21
     */
    AddressBookDto getDtoById(String id);

    /**
     * 批量新增
     *
     * @param rows 批量新增入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean saveDtoBatch(List<AddressBookDto> rows);

    /**
     * 更新
     *
     * @param addressBookEditParam 更新入参实体类
     * @return true - 操作成功 false -操作失败
     * @since 2023-04-21
     */
    boolean updateByParam(AddressBookEditParam addressBookEditParam);

    boolean isAlreadyExist(AddressBookQueryParam addressBookQueryParam);
}