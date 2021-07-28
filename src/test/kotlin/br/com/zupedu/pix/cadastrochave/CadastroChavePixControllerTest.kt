package br.com.zupedu.pix.cadastrochave

import br.com.zup.edu.*
import br.com.zupedu.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class CadastroChavePixControllerTest(
) {
    @Inject
    lateinit var clientRegistraGrpc: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient

    @BeforeEach
    fun setup() {

    }

    @Test
    fun `deve cadastrar uma nova chave pix`() {
        // cenario
        val idCliente = UUID.randomUUID()
        val pixId = UUID.randomUUID()

        val novaChaveRequest = NovaChavePixRequest(
            tipo = TipoDeChaveRequest.CELULAR,
            chave = "+73433672008",
            tipoDeConta = TipoDeContaRequest.CONTA_CORRENTE
        )

        val request = HttpRequest.POST("/api/v1/clientes/${idCliente}/pix", novaChaveRequest)

        Mockito.`when`(clientRegistraGrpc.cadastrar(Mockito.any()))
            .thenReturn(
                RegistraChavePixResponse.newBuilder()
                    .setIdCliente(idCliente.toString())
                    .setIdPix(pixId.toString())
                    .build()
            )

        // acao
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        // validao
        with(response) {
            assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location").contains(pixId.toString()))
        }
    }

    @Test
    fun `nao deve cadastrar chave pix com dados invalidos`() {
        // cenario
        val idCliente = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val body = object {
            val tipo = TipoDeChaveRequest.CELULAR
            val chave = "73433672"
            val tipoDeConta = TipoDeContaRequest.CONTA_CORRENTE
        }
        val request = HttpRequest.POST("/api/v1/clientes/${idCliente}/pix", body)

        Mockito.`when`(
            clientRegistraGrpc.cadastrar(
                RegistraChavePixRequest.newBuilder()
                    .setIdCliente(idCliente)
                    .setTipo(TipoChave.valueOf(body.tipo.name))
                    .setChave(body.chave)
                    .setTipoConta(TipoConta.valueOf(body.tipoDeConta.name))
                    .build()
            )
        ).thenReturn(RegistraChavePixResponse.newBuilder().build())

        // acao
        val exception = assertThrows<HttpClientResponseException> {
            clientHttp.toBlocking().exchange(request, Any::class.java)
        }

        // validao
        with(exception) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class MockFactoryGrpcClient {
        @Singleton
        fun mockRegistra(): KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub {
            return Mockito.mock(KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java)
        }
    }

}