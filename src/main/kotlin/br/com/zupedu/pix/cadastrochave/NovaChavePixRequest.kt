package br.com.zupedu.pix.cadastrochave

import br.com.zup.edu.RegistraChavePixRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zupedu.pix.validacoescustomizadas.ValidUUID
import br.com.zupedu.pix.validacoescustomizadas.ValidarChavePix
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidarChavePix
@Introspected
data class NovaChavePixRequest(
    @field:NotNull
    val tipo: TipoDeChaveRequest?,

    @Size(max = 77)
    val chave: String?,

    @field:NotNull
    val tipoDeConta: TipoDeContaRequest?
) {

    fun converteParaModeloGrpc(@ValidUUID @NotBlank idCliente: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setIdCliente(idCliente.toString())
            .setTipo(tipo?.atributoGrpc ?: TipoChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave ?: "")
            .setTipoConta(tipoDeConta?.atributoGrpc ?: TipoConta.UNKNOWN_TIPO_CONTA)
            .build()
    }
}

enum class TipoDeContaRequest(val atributoGrpc: TipoConta) {
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}

