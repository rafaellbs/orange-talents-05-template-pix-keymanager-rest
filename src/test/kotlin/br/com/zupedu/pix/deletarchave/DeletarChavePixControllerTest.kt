package br.com.zupedu.pix.deletarchave

import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.RemoveChavePixResponse
import br.com.zupedu.GrpcClientFactory
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus

@MicronautTest
class DeletarChavePixControllerTest {

    @field:Inject
    lateinit var removeClientGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve deletar uma chave pix com idCliente e PixId existentes`() {
        val idCliente = UUID.randomUUID()
        val pixId = UUID.randomUUID()

        val request = HttpRequest.DELETE("/api/v1/clientes/$idCliente/pix/$pixId", null)

        Mockito.`when`(removeClientGrpc.remover(Mockito.any())).thenReturn(
            RemoveChavePixResponse.newBuilder()
                .setIdCliente(idCliente.toString())
                .setChave(pixId.toString())
                .build()
        )

        val response = client.toBlocking().exchange(request, Any::class.java)

        with(response) {
            assertEquals(HttpStatus.OK, status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class ClientsGrpc {
        @Singleton
        fun mockRemoveGrpc() : KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub {
            return Mockito.mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
        }
    }
}