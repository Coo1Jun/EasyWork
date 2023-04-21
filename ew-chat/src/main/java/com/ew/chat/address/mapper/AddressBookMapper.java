package com.ew.chat.address.mapper;

import com.ew.chat.address.dto.AddressBookDto;
import com.ew.chat.address.dto.AddressBookQueryParam;
import com.ew.chat.address.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    /**
     * 分页获取通讯录
     */
    List<AddressBookDto> getAddressBookList(@Param("user_id") String userId, @Param("query") AddressBookQueryParam addressBookQueryParam);

    /**
     * 获取通讯录总数
     * @param userId
     * @param addressBookQueryParam
     * @return
     */
    Integer getAddressBookListCount(@Param("user_id") String userId, @Param("query") AddressBookQueryParam addressBookQueryParam);

}
