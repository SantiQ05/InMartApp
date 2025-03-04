package com.example.aplicacioninmartiu.inmart_logic.holders

import com.example.aplicacioninmartiu.inmart_logic.Inmart

object AppObject {
    private var app: Inmart? = null

    fun setApp(inmart: Inmart) {
        app = inmart
    }

    fun getApp(): Inmart? {
        return app
    }
}