package top.rainine.homebangumi.core.common.file.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.api.req.PreViewFilesReq;
import top.rainine.homebangumi.api.resp.IsEmptyDirResp;
import top.rainine.homebangumi.api.resp.PreViewFilesResp;
import top.rainine.homebangumi.common.utils.HbFileNameUtils;
import top.rainine.homebangumi.core.common.file.FilePreViewService;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @authoer rainine
 * @date 2024/10/9 23:23
 * @desc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FilePreViewServiceImpl implements FilePreViewService {

    @Override
    public PreViewFilesResp preViewFiles(PreViewFilesReq req) {
        if (!HbFileNameUtils.isValidFileName(req.getPath())) {
            throw new HbBizException(HbCodeEnum.PRE_VIEW_FILES_PATH_INVALID);
        }

        Path rootPath = Paths.get(req.getPath());

        PreViewFilesResp resp = new PreViewFilesResp()
                .setPath(req.getPath())
                .setName(rootPath.getFileName().toString());

        // 如果不存在，也视为合法的目录
        if (Files.notExists(rootPath)) {
            return resp.setIsFile(false)
                    .setChildren(new ArrayList<>());
        }

        // 如果不是目录
        if (!Files.isDirectory(rootPath)) {
            return resp.setIsFile(true);
        }

        resp.setIsFile(false);


        try (Stream<Path> paths = Files.walk(rootPath, 1)) {
            List<PreViewFilesResp.ChildFileInfo> childFileInfos = paths
                    .filter(path -> !Objects.equals(path, rootPath))
                    .map(path -> new PreViewFilesResp.ChildFileInfo()
                            .setPath(path.toString())
                            .setName(path.getFileName().toString())
                            .setIsFile(!Files.isDirectory(path)))
                    .toList();
            resp.setChildren(childFileInfos);
        } catch (IOException e) {
            log.error("[FilePreViewServiceImpl]walk file children failed, path: {}", req.getPath(), e);
            throw new HbBizException(HbCodeEnum.WALK_FILE_PATH_FAILED);
        }


        return resp;
    }

    @Override
    public IsEmptyDirResp isEmptyDir(PreViewFilesReq req) {
        if (!HbFileNameUtils.isValidFileName(req.getPath())) {
            throw new HbBizException(HbCodeEnum.PRE_VIEW_FILES_PATH_INVALID);
        }

        Path rootPath = Paths.get(req.getPath());

        IsEmptyDirResp resp = new IsEmptyDirResp();

        // 如果不存在，也视为合法的目录
        if (Files.notExists(rootPath)) {
            return resp.setIsFile(false).setIsEmpty(true);
        }

        // 如果不是目录
        if (!Files.isDirectory(rootPath)) {
            return resp.setIsFile(true);
        }

        try (Stream<Path> paths = Files.list(rootPath)) {
            return resp.setIsFile(false)
                    .setIsEmpty(paths.findFirst().isEmpty());
        } catch (IOException e) {
            log.error("[FilePreViewServiceImpl]check is empty dir failed, path: {}", req.getPath(), e);
            throw new HbBizException(HbCodeEnum.WALK_FILE_PATH_FAILED);
        }

    }
}


