package br.com.zupedu.pix.cadastrochave

import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{idCliente}")
class CadastroChavePixController(
    @Inject val clientGrpc: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub
) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun cadastrar(@PathVariable idCliente: UUID, @Body @Valid request: NovaChavePixRequest): HttpResponse<Any> {
        LOGGER.info("[$idCliente] Cadastrando chave pix com $request")

        val responseGrpc = clientGrpc.cadastrar(request.converteParaModeloGrpc(idCliente))

        return HttpResponse.created(
            location(
                idCliente = idCliente,
                pixId = responseGrpc.idPix
            )
        )
    }

    private fun location(idCliente: UUID, pixId: String) =
        HttpResponse.uri("/api/v1/clientes/${idCliente}/pix/${pixId}")
}
