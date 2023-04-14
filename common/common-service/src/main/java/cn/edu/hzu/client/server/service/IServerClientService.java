package cn.edu.hzu.client.server.service;


import cn.edu.hzu.client.dto.FileMetaDto;
import cn.edu.hzu.client.dto.FileMetaEditParam;
import cn.edu.hzu.client.dto.UserDto;
import cn.edu.hzu.common.api.RestResponse;

import java.util.List;


public interface IServerClientService {

    /**
     * 根据用户id获取用户基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param id 用户id
     * @return
     * @author lzf
     * @date: 2023/3/31 14:58
     */
    RestResponse<UserDto> getUserDtoByIdClient(boolean throwEx, String id);
    default UserDto getUserDtoById(String id) {
        RestResponse<UserDto> response = getUserDtoByIdClient(true, id);
        return response.getData();
    }

    /**
     * 根据用户id集合获取用户基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param ids 用户id集合
     * @return
     * @author lzf
     * @date: 2023/4/8 14:58
     */
    RestResponse<List<UserDto>> getUserListByIdsClient(boolean throwEx, List<String> ids);
    default List<UserDto> getUserListById(List<String> ids) {
        RestResponse<List<UserDto>> response = getUserListByIdsClient(true, ids);
        return response.getData();
    }

    /**
     * 根据文件id集合获取文件基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param id 文件id集合
     * @return
     * @author lzf
     * @date: 2023/4/9 14:58
     */
    RestResponse<FileMetaDto> getFileByIdClient(boolean throwEx, String id);
    default FileMetaDto getFileById(String id) {
        RestResponse<FileMetaDto> response = getFileByIdClient(true, id);
        return response.getData();
    }

    /**
     * 根据文件id集合获取文件基本信息
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param ids 文件id集合
     * @return
     * @author lzf
     * @date: 2023/4/9 14:58
     */
    RestResponse<List<FileMetaDto>> getFileListByIdsClient(boolean throwEx, List<String> ids);
    default List<FileMetaDto> getFileListByIds(List<String> ids) {
        RestResponse<List<FileMetaDto>> response = getFileListByIdsClient(true, ids);
        return response.getData();
    }

    /**
     * 根据文件id修改文件名
     * @param throwEx 是否抛出异常（当程序报错时）
     * @param
     * @return
     * @author lzf
     * @date: 2023/4/9 14:58
     */
    RestResponse<Boolean> renameFileClient(boolean throwEx, FileMetaEditParam fileMetaEditParam);
    default Boolean renameFile(FileMetaEditParam fileMetaEditParam) {
        RestResponse<Boolean> response = renameFileClient(true, fileMetaEditParam);
        return response.getData();
    }
}
