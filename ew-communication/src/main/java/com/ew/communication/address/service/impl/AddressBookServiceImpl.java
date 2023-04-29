package com.ew.communication.address.service.impl;

import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.client.server.service.IServerClientService;
import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.utils.StringUtils;
import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.BaseEntity;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.common.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ew.communication.address.dto.*;
import com.ew.communication.address.entity.AddressBook;
import com.ew.communication.address.mapper.AddressBookMapper;
import com.ew.communication.address.service.IAddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private IServerClientService serverClientService;

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
    public boolean isAlreadyExist(AddressBookQueryParam addressBookQueryParam) {
        int count = this.count(Wrappers.<AddressBook>lambdaQuery()
                .eq(AddressBook::getUserId, addressBookQueryParam.getUserId())
                .eq(AddressBook::getFromId, UserUtils.getCurrentUser().getUserid()));
        if (count > 0) {
            throw CommonException.builder().resultCode(CommonErrorEnum.ANY_MESSAGE.setParams(new Object[]{"该用户已经在你通讯录当中"})).build();
        }
        return true;
    }

    @Override
    public List<AddressBookDto> memberList() {
        // 定义一个map，key为用户id，给成员列表去重
        Map<String, AddressBookDto> resultMap = new HashMap<>();
        String curUserId = UserUtils.getCurrentUser().getUserid();
        // 找出个人通讯录中的所有成员
        List<AddressBook> personAddressBook = this.list(Wrappers.<AddressBook>lambdaQuery().eq(AddressBook::getFromId, curUserId));
        if (CollectionUtils.isNotEmpty(personAddressBook)) {
            for (AddressBook addressBook : personAddressBook) {
                if (StringUtils.isNotEmpty(addressBook.getUserId())) {
                    UserDto userInfo = serverClientService.getUserDtoById(addressBook.getUserId());
                    AddressBookDto dto = new AddressBookDto();
                    dto.setId(userInfo.getId());
                    dto.setName(userInfo.getRealName());
                    dto.setAvatar(userInfo.getPortrait());
                    dto.setEmail(userInfo.getEmail());
                    dto.setDescription(userInfo.getDescription());
                    resultMap.put(dto.getId(), dto);
                }
            }
        }
        // 查出当前用户所在的所有项目组中的所有成员列表
        List<AddressBookDto> groupAddressBookList = this.baseMapper.getAddressBookListByGroup(curUserId);
        if (CollectionUtils.isNotEmpty(groupAddressBookList)) {
            for (AddressBookDto addressBookDto : groupAddressBookList) {
                resultMap.put(addressBookDto.getId(), addressBookDto);
            }
        }
        return new ArrayList<>(resultMap.values());
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
