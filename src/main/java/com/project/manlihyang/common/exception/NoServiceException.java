package com.project.manlihyang.common.exception;

import com.project.manlihyang.util.AppConst;

/**
 * [phantasmicmeans] created on 22/12/2019
 */
public class NoServiceException extends RuntimeException {
    public NoServiceException() {
        super(AppConst.NO_SERVICE);
    }
}
