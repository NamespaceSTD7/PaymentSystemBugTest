package com.example.sellapiscreentest

import ru.evotor.framework.core.IntegrationService
import ru.evotor.framework.core.action.event.receipt.payment.combined.PaymentDelegatorEventProcessor
import ru.evotor.framework.core.action.event.receipt.payment.combined.event.PaymentDelegatorEvent
import ru.evotor.framework.core.action.event.receipt.payment.combined.result.PaymentDelegatorCanceledAllEventResult
import ru.evotor.framework.core.action.processor.ActionProcessor

class CombinedPaymentService : IntegrationService() {
    override fun createProcessors(): MutableMap<String, ActionProcessor> {
        val context = this

        return mutableMapOf(PaymentDelegatorEvent.NAME_ACTION to object : PaymentDelegatorEventProcessor() {

            override fun call(action: String, event: PaymentDelegatorEvent, callback: Callback) {
                try {
                    callback.startActivity(ComboPaymentActivity.start(context))

                } catch (e: Exception) {
                    callback.onResult(PaymentDelegatorCanceledAllEventResult(null))
                }
            }
        })
    }
}
