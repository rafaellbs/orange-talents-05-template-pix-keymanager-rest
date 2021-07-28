package br.com.zupedu.pix.deletarchave

import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/{idCliente}/pix/{pixId}")
class DeletarChavePixController(
    @Inject val clientRemoveGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete
    fun deletar(@PathVariable idCliente: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        LOGGER.info("[$idCliente] deletando chave de PixId: $pixId")

        clientRemoveGrpc.remover(
            RemoveChavePixRequest.newBuilder()
                .setIdCliente(idCliente.toString())
                .setIdPix(pixId.toString())
                .build()
        )

        return HttpResponse.ok()
    }
}