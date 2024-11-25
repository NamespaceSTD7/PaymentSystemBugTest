package com.example.sellapiscreentest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import ru.evotor.framework.component.PaymentPerformer
import ru.evotor.framework.core.IntegrationAppCompatActivity
import ru.evotor.framework.core.action.event.receipt.payment.combined.result.PaymentDelegatorCanceledAllEventResult
import ru.evotor.framework.core.action.event.receipt.payment.combined.result.PaymentDelegatorSelectedEventResult
import ru.evotor.framework.payment.PaymentPurpose
import ru.evotor.framework.payment.PaymentSystem
import ru.evotor.framework.payment.PaymentType
import java.math.BigDecimal
import java.util.*

class ComboPaymentActivity : IntegrationAppCompatActivity() {

    private val valueToPay: BigDecimal
        get() = 99999.toBigDecimal()

    val cash: PaymentPerformer
        get() {
            val paymentSystem = PaymentSystem(
                PaymentType.CASH,
                PaymentType.CASH.name,
                "ru.evotor.paymentSystem.cash.base"
            )
            return PaymentPerformer(paymentSystem, null, null, null, PaymentType.CASH.name)
        }

//    val card: PaymentPerformer
//        get() {
//            val paymentSystem = PaymentSystem(
//                PaymentType.ELECTRON,
//                PaymentType.ELECTRON.name,
//                "ru.evotor.paymentSystem.cashless.base"
//            )
//            return PaymentPerformer(paymentSystem, null, null, null, PaymentType.ELECTRON.name)
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combo_payment)

        findViewById<Button>(R.id.buttonCash).setOnClickListener {
            actionPay(PaymentType.CASH.name)
        }
//        findViewById<Button>(R.id.buttonCard).setOnClickListener {
//            actionPay(PaymentType.ELECTRON.name)
//        }

        findViewById<TextView>(R.id.textToPay).text = valueToPay.toString()
    }

    override fun onBackPressed() {
        setIntegrationResult(PaymentDelegatorCanceledAllEventResult(null))
        super.onBackPressed()
    }

    private fun actionPay(paymentType: String) {
        makePayment(PaymentType.valueOf(paymentType), valueToPay)
    }

    private fun makePayment(
        paymentType: PaymentType,
        value: BigDecimal
    ) {
        val paymentPerformer: PaymentPerformer = when (paymentType) {
            PaymentType.CASH -> cash
            //PaymentType.ELECTRON -> card
            else -> throw NotImplementedError("Вид оплаты $paymentType не поддерживается.")
        }

        val paymentPurpose =
            PaymentPurpose(
                UUID.randomUUID().toString(),
                paymentPerformer.paymentSystem!!.paymentSystemId,
                paymentPerformer,
                value,
                null,
                null
            )

        setIntegrationResult(
            PaymentDelegatorSelectedEventResult(paymentPurpose, null)
        )
        finish()
    }

    companion object {
        fun start(context: Context): Intent {
            val starter = Intent(context, ComboPaymentActivity::class.java)
            return starter
        }
    }

}