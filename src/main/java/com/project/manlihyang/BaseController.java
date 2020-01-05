package com.project.manlihyang;

import com.project.manlihyang.common.ResponseData;
import com.project.manlihyang.util.ApiHelper;
import com.project.manlihyang.util.AppConst;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * RestController 공통 모듈
 * [U - UserService / B - Board Service / C - Chat Service]
 */
public class BaseController {
    @Autowired
    private ApiHelper helper;

    /**
     * 성공 Response Format
     * @return ResponseData
     */
    protected ResponseData<Void> successResponseU() {
        return ResponseData.<Void>builder()
                .timeStamp(helper.makeTimeStamp())
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .build();
    }

    /**
     * 실패 Response Format
     * @param message 상세 메시지
     * @return ResponseData
     */
    protected ResponseData<Void> failedResponseU(String message) {
        return ResponseData.<Void>builder()
                .timeStamp(helper.makeTimeStamp())
                .serviceCode(AppConst.USER_SERVICE)
                .message(message)
                .build();
    }

    /**
     * 성공 Response Format With data
     * @param data response data
     * @param <T> Type
     * @return ResponseData
     */
    protected <T> ResponseData<T> successResponseBoard(T data) {
        return ResponseData.<T>builder()
                .timeStamp(helper.makeTimeStamp())
                .serviceCode(AppConst.BOARD_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .data(data)
                .build();
    }

    /**
     * 실패 Response Format
     * @return ResponseData
     */
    protected ResponseData<Void> failedResponseBoard(String message) {
        return ResponseData.<Void>builder()
                .timeStamp(helper.makeTimeStamp())
                .serviceCode(AppConst.BOARD_SERVICE)
                .message(message)
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
                .timeStamp(helper.makeTimeStamp())
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
                .timeStamp(helper.makeTimeStamp())
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
                .timeStamp(helper.makeTimeStamp())
                .serviceCode(AppConst.USER_SERVICE)
                .message(AppConst.OK_RESPONSE)
                .data(data)
                .build();
    }
}
