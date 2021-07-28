package br.com.zupedu.pix.listarchaves

import br.com.zup.edu.*
import br.com.zupedu.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class ListarChavePixControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var listarChavesGrpcClient: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub

    companion object {
        val ID_CLIENTE = UUID.randomUUID()
    }

    @Test
    fun `deve listar todas chaves pix de um usuario valido`() {
        val request = HttpRequest.GET<String>("/api/v1/clientes/$ID_CLIENTE/pix")

        Mockito.`when`(
            listarChavesGrpcClient.listar(
                ListarChavePixRequest.newBuilder().setIdCliente(ID_CLIENTE.toString()).build()
            )
        ).thenReturn(
            ListarChavePixResponse.newBuilder()
                .addAllChaves(listaChavePixResponse())
                .build()
        )

        val response = client.toBlocking().exchange(request, List::class.java)

        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(4, body().size)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class ClientsGrpc {
        @Singleton
        fun listarChavePixStub() =
            Mockito.mock(KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub::class.java)
    }

    fun listaChavePixResponse(): MutableList<ListarChavePixResponse.ChavePix> {
        val lista = mutableListOf<ListarChavePixResponse.ChavePix>()

        for (i in 1..4) {
            lista.add(
                ListarChavePixResponse.ChavePix.newBuilder()
                    .setIdCliente(ID_CLIENTE.toString())
                    .setPixId(UUID.randomUUID().toString())
                    .setTipo(TipoChave.ALEATORIA)
                    .setChave(UUID.randomUUID().toString())
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setCriadaEm(
                        Timestamp.newBuilder().setSeconds(LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond)
                            .setNanos(LocalDateTime.now().nano)
                    )
                    .build()
            )
        }

        return lista
    }
}