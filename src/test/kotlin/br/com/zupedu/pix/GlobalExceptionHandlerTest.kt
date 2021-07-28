package br.com.zupedu.pix

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GlobalExceptionHandlerTest {
    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 404 quando statusException for not found`() {
        val mensagem = "nao encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        with(resposta) {
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertNotNull(body())
            assertEquals(mensagem, (body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 422 quando statusException for already exists`() {
        val mensagem = "chave pix já existe"
        val alreadyExistsException = StatusRuntimeException(
            Status.ALREADY_EXISTS.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, alreadyExistsException)

        with(resposta) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
            assertNotNull(body())
            assertEquals(mensagem, (body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 400 quando statusException for invalid argument`() {
        val mensagem = "nao encontrado"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)

        with(resposta) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
            assertNotNull(body())
            assertEquals(mensagem, (body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 500 quando lancado qualquer outro erro`() {
        val mensagem = "nao encontrado"
        val internalException = StatusRuntimeException(Status.INTERNAL.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, internalException)

        with(resposta) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
        }
    }
}