package com.ew.communication.address.service.impl;

import cn.edu.hzu.common.api.utils.UserUtils;
import com.ew.communication.address.entity.AddressBook;
import com.ew.communication.address.mapper.AddressBookMapper;
import com.ew.communication.address.service.IAddressBookService;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.ew.communication.address.dto.AddressBookQueryParam;
import com.ew.communication.address.dto.AddressBookAddParam;
import com.ew.communication.address.dto.AddressBookEditParam;
import com.ew.communication.address.dto.AddressBookParamMapper;
import com.ew.communication.address.dto.AddressBookDto;
import cn.edu.hzu.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.edu.hzu.common.api.PageResult;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author LiZhengFan
 * @since 2023-04-21
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class, Error.class})
public class AddressBookServiceImpl extends BaseServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Autowired
    private AddressBookParamMapper addressBookParamMapper;

    @Override
    public PageResult<AddressBookDto> pageDto(AddressBookQueryParam queryParam) {
        String userId = UserUtils.getCurrentUser().getUserid();
        queryParam.setOffset((queryParam.getPageNo() - 1) * queryParam.getLimit());
        List<AddressBookDto> dtoList = this.baseMapper.getAddressBookList(userId, queryParam);
        if (dtoList == null) return null;
        int total = this.baseMapper.getAddressBookListCount(userId, queryParam);
        return PageResult.<AddressBookDto>builder().records(dtoList).total(total).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveByParam(AddressBookAddParam addressBookAddParam) {
        AddressBook addressBook = addressBookParamMapper.addParam2Entity(addressBookAddParam);
        // Assert.notNull(ResultCode.PARAM_VALID_ERROR,addressBook);
        return save(addressBook);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean updateByParam(AddressBookEditParam addressBookEditParam) {
        AddressBook addressBook = addressBookParamMapper.editParam2Entity(addressBookEditParam);
        addressBook.setFromId(UserUtils.getCurrentUser().getUserid());
        return updateById(addressBook);
    }

    @Override
    public AddressBookDto getDtoById(String id) {
        return addressBookParamMapper.entity2Dto(this.getById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public boolean saveDtoBatch(List<AddressBookDto> rows) {
        return saveBatch(addressBookParamMapper.dtoList2Entity(rows));
    }

    private Wrapper<AddressBook> getPageSearchWrapper(AddressBookQueryParam addressBookQueryParam) {
        LambdaQueryWrapper<AddressBook> wrapper = Wrappers.<AddressBook>lambdaQuery();
        if (BaseEntity.class.isAssignableFrom(AddressBook.class)) {
            wrapper.orderByDesc(AddressBook::getUpdateTime, AddressBook::getCreateTime);
        }
        return wrapper;
    }
}
