package top.rainine.homebangumi.web.configuration;

import cn.dev33.satoken.exception.NotLoginException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.rainine.homebangumi.api.resp.Result;
import top.rainine.homebangumi.def.exception.HbBizException;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/3/21 23:54
 * @desc 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = HbBizException.class)
    @ResponseBody
    public Result<Void> bizExceptionHandler(HttpServletRequest req, HbBizException e){
        return Result.createError(e.getCode(), e.getMessage());
    }


    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { BindException.class, ValidationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public Result<Void> handleParameterVerificationException(Exception e) {
        HbCodeEnum codeEnum = HbCodeEnum.INVALID_PARAM;
        if (e instanceof BindException be) {
            BindingResult bindingResult = be.getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            if (Objects.nonNull(fieldError)) {
                codeEnum = HbCodeEnum.getCode(fieldError.getDefaultMessage(), HbCodeEnum.INVALID_PARAM);
            }
        } else if (e instanceof ConstraintViolationException) {
            codeEnum = HbCodeEnum.getCode(e.getMessage(), HbCodeEnum.INVALID_PARAM);
        } else if (e instanceof HttpMessageNotReadableException) {
            codeEnum = HbCodeEnum.getCode(e.getMessage(), HbCodeEnum.INVALID_PARAM);
        }
        return Result.createError(codeEnum);
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { NotLoginException.class})
    public Result<Void> handleNotLoginException(Exception e) {
        return Result.createError(HbCodeEnum.UNAUTHORIZED);
    }


    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<Void> exceptionHandler(HttpServletRequest req, Exception e){
        log.error("[GlobalExceptionHandler]unknown exception ",e);
        return Result.createError(HbCodeEnum.INTERNAL_SERVER_ERROR);
    }
}
