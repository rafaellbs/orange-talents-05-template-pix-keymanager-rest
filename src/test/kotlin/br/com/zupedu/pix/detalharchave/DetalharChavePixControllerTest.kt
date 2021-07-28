package br.com.zupedu.pix.detalharchave

import br.com.zup.edu.BuscaChavePixRequest
import br.com.zup.edu.BuscaChavePixResponse
import br.com.zup.edu.KeyManagerBuscaGrpcServiceGrpc
import br.com.zupedu.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class DetalharChavePixControllerTest {

    @field:Inject
    lateinit var clientGrpc: KeyManagerBuscaGrpcServiceGrpc.KeyManagerBuscaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient

    @Test
    fun `deve detalhar uma chave pix com idPix e idCliente validos`() {
        val idCliente = UUID.randomUUID()
        val idPix = UUID.randomUUID()

        Mockito.`when`(
            clientGrpc.buscar(
                BuscaChavePixRequest.newBuilder()
                    .setChavePix(
                        BuscaChavePixRequest.FiltroPorChavePix.newBuilder()
                            .setIdCliente(idCliente.toString())
                            .setPixId(idPix.toString())
                            .build()
                    )
                    .build()
            )
        ).thenReturn(BuscaChavePixResponse.newBuilder()
            .setIdCliente(idCliente.toString())
            .setPixId(idPix.toString())
            .build())

        val request = HttpRequest.GET<String>("/api/v1/clientes/$idCliente/pix/$idPix")

        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        with(response) {
            Assertions.assertEquals(HttpStatus.OK, status)
            Assertions.assertTrue(body.isPresent)
        }
    }



    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class Clients {
        @Singleton
        fun mockDetalheStubGrpc(): KeyManagerBuscaGrpcServiceGrpc.KeyManagerBuscaGrpcServiceBlockingStub {
            return Mockito.mock(KeyManagerBuscaGrpcServiceGrpc.KeyManagerBuscaGrpcServiceBlockingStub::class.java)
        }
    }
}
