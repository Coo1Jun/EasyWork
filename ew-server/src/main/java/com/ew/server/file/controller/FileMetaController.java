package com.ew.server.file.controller;


import cn.edu.hzu.common.api.PageResult;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.utils.ExcelUtils;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ew.server.constants.OSSConstant;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import com.ew.server.file.dto.FileMetaQueryParam;
import com.ew.server.file.dto.FileMetaAddParam;
import com.ew.server.file.dto.FileMetaEditParam;
import com.ew.server.file.dto.FileMetaDto;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.ew.server.file.service.IFileMetaService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件元信息控制器
 * </p>
 *
 * @author LiZhengFan
 * @since 2023-03-01
 *
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件元信息服务接口")
public class FileMetaController {

    @Autowired
    private IFileMetaService fileMetaService;

    /**
     *
     * 分页获取文件元信息列表
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation("分页获取文件元信息列表")
    @GetMapping("/list")
    public RestResponse<PageResult<FileMetaDto>> pageList(@Valid FileMetaQueryParam fileMetaQueryParam){
        return RestResponse.ok(fileMetaService.pageDto(fileMetaQueryParam));
    }

    /**
     *
     * 新增文件元信息
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation("新增文件元信息")
    @PostMapping
    public RestResponse<Boolean> add(@RequestBody @Valid FileMetaAddParam fileMetaAddParam) {
        return RestResponse.ok(fileMetaService.saveByParam(fileMetaAddParam));
    }

    /**
     *
     * 修改文件元信息
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation(value = "修改文件元信息")
    @PutMapping
    public RestResponse<Boolean> update(@RequestBody @Valid FileMetaEditParam fileMetaEditParam) {
        return RestResponse.ok(fileMetaService.updateByParam(fileMetaEditParam));
    }

    /**
     *
     * 根据id删除文件元信息
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation(value = "根据id删除文件元信息")
    @DeleteMapping("/{ids}")
    public RestResponse<Boolean> delete(@PathVariable("ids") @NotEmpty String[] ids) {
        return RestResponse.ok(fileMetaService.removeByIds(Arrays.asList(ids)));
    }

    /**
     *
     * 根据id获取文件元信息
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation(value = "根据id获取文件元信息")
    @GetMapping(value = "/{id}")
    public RestResponse<FileMetaDto> get(@PathVariable String id) {
        return RestResponse.ok(fileMetaService.getDtoById(id));
    }


    /**
     *
     * 上传文件
     *
     * @author LiZhengFan
     * @since 2023-03-01
     *
     */
    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public RestResponse<FileMetaDto> uploadFile(@RequestParam("file") MultipartFile file){
        FileMetaDto dto = fileMetaService.upload(file, OSSConstant.FILE_DIRECTORY);
        if (dto == null) {
            return RestResponse.failed();
        }
        return RestResponse.ok(dto);
    }

    /**
     *
     * 下载文件
     *
     * @author LiZhengFan
     * @since 2023-03-02
     *
     */
    @ApiOperation(value = "上传文件")
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable("id") @NotEmpty String id, HttpServletResponse response){
        fileMetaService.download(id, response, OSSConstant.FILE_DIRECTORY);
    }

    /**
     *
     * 删除文件
     *
     * @author LiZhengFan
     * @since 2023-03-02
     *
     */
    @ApiOperation(value = "删除文件")
    @DeleteMapping("/delete/{ids}")
    public RestResponse<Boolean> deleteFile(@PathVariable("ids") @NotEmpty String[] ids){
        return RestResponse.ok(fileMetaService.delete(Arrays.asList(ids), OSSConstant.FILE_DIRECTORY));
    }

}
