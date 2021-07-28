package br.com.zupedu.pix.detalharchave

import br.com.zup.edu.BuscaChavePixRequest
import br.com.zup.edu.KeyManagerBuscaGrpcServiceGrpc
import br.com.zup.edu.TipoChave
import com.google.protobuf.Timestamp
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/{idCliente}/pix/{pixId}")
class DetalharChavePixController(
    @Inject val buscaGrpcClient: KeyManagerBuscaGrpcServiceGrpc.KeyManagerBuscaGrpcServiceBlockingStub
) {

    @Get
    fun detalhar(@PathVariable idCliente: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        val responseGrpc = buscaGrpcClient.buscar(
            BuscaChavePixRequest.newBuilder()
                .setChavePix(
                    BuscaChavePixRequest.FiltroPorChavePix.newBuilder()
                        .setIdCliente(idCliente.toString())
                        .setPixId(pixId.toString())
                        .build()
                )
                .build()
        )

        val detalheChave = DetalheChavePixResponse(responseGrpc)

        return HttpResponse.ok(detalheChave)
    }
}