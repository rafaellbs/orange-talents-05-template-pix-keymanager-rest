package br.com.zupedu.pix.detalharchave

import br.com.zup.edu.BuscaChavePixResponse
import br.com.zup.edu.TipoConta
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DetalheChavePixResponse(chavePixResponse: BuscaChavePixResponse) {

    val pixId = chavePixResponse.pixId
    val idCliente = chavePixResponse.idCliente
    val tipo = chavePixResponse.chave.tipo

    val criadaEm = chavePixResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (chavePixResponse.chave.conta.tipo) {
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANÇA"
        else -> "CONTA_DESCONHECIDA"
    }

    val conta = mapOf(
        Pair("tipo", tipoConta),
        Pair("instituicao", chavePixResponse.chave.conta.instituicao),
        Pair("nomeDoTitulat", chavePixResponse.chave.conta.nomeDoTitular),
        Pair("cpfDoTitular", chavePixResponse.chave.conta.cpfDoTitular),
        Pair("agencia", chavePixResponse.chave.conta.cpfDoTitular),
        Pair("numero", chavePixResponse.chave.conta.numeroDaConta)
    )
}