package com.project.manlihyang;

import com.project.manlihyang.common.ResponseData;
import com.project.manlihyang.util.AppConst;

/**
 * RestController 공통 모듈
 * [U - UserService / B - Board Service / C - Chat Service]
 */
public class BaseController {

    /**
     * 성공 Response Format
     * @return ResponseData
     */
    protected ResponseData<Void> successResponseU() {
        return ResponseData.<Void>builder()
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .build();
    }

    /**
     * 성공 Response Format With data
     * @param data response data
     * @param <T> Type
     * @return ResponseData
     */
    protected <T> ResponseData<T> successResponseU(T data) {
        return ResponseData.<T>builder()
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .data(data)
                .build();
    }

    /**
     * 성공 Response Format
     * @return ResponseData
     */
    protected ResponseData<Void> successResponseB() {
        return ResponseData.<Void>builder()
                .serviceCode(AppConst.BOARD_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .build();
    }

    /**
     * 성공 Response Format With data
     * @param data response data
     * @param <T> Type
     * @return ResponseData
     */
    protected <T> ResponseData<T> successResponseB(T data) {
        return ResponseData.<T>builder()
                .serviceCode(AppConst.BOARD_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .data(data)
                .build();
    }    /**
     * 성공 Response Format
     * @return ResponseData
     */
    protected ResponseData<Void> successResponseC() {
        return ResponseData.<Void>builder()
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .build();
    }

    /**
     * 성공 Response Format With data
     * @param data response data
     * @param <T> Type
     * @return ResponseData
     */
    protected <T> ResponseData<T> successResponseC(T data) {
        return ResponseData.<T>builder()
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .data(data)
                .build();
    }
}
