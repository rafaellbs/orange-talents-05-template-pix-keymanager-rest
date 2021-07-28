package br.com.zupedu.pix.listarchaves

import br.com.zup.edu.*
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/{idCliente}/pix")
class ListarChavePixController(
    @Inject val listarGrpcClient: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub
) {

    @Get
    fun listar(@PathVariable idCliente: UUID): HttpResponse<Any> {

        val chaves = listarGrpcClient.listar(
            ListarChavePixRequest.newBuilder()
                .setIdCliente(idCliente.toString())
                .build()
        ).chavesList.map { ChavePixResponse(it) }

        return HttpResponse.ok(chaves)
    }
}

@Introspected
class ChavePixResponse(chave: ListarChavePixResponse.ChavePix) {
    val pixId: String = chave.pixId
    val idCliente: String = chave.idCliente
    val tipoChave: TipoChave = chave.tipo
    val valorChave: String = chave.chave
    val tipoConta: TipoConta = chave.tipoConta
    val criadaEm: LocalDateTime = chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}