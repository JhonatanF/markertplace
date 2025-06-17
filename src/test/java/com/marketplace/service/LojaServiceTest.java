package com.marketplace.service;

import com.marketplace.model.Loja;
import com.marketplace.repository.LojaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LojaServiceTest {

    @TempDir
    Path tempDir;

    private LojaRepository repository;
    private LojaService service;
    private Loja loja;

    @BeforeEach
    void setUp() {
        repository = new LojaRepository();
        service = new LojaService(repository);
        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901", "Rua Teste, 123");
        service.removerLojas(); // garante ambiente limpo
    }

    @Test
    void testCadastrarLoja() {
        Loja cadastrada = service.cadastrar(loja);

        assertEquals(loja.getNome(), cadastrada.getNome());
        assertEquals(loja.getEmail(), cadastrada.getEmail());
        assertEquals(loja.getSenha(), cadastrada.getSenha());
        assertEquals(loja.getCpfCnpj(), cadastrada.getCpfCnpj());
        assertEquals(loja.getEndereco(), cadastrada.getEndereco());

        Optional<Loja> encontrada = service.buscarPorId(loja.getCpfCnpj());
        assertTrue(encontrada.isPresent());
    }

    @Test
    void testBuscarLoja() {
        service.cadastrar(loja);

        Optional<Loja> encontrada = service.buscarPorId(loja.getCpfCnpj());
        assertTrue(encontrada.isPresent());
        assertEquals(loja.getNome(), encontrada.get().getNome());
    }

    @Test
    void testBuscarPorNome_CaseInsensitive() {
        service.cadastrar(loja);
        Loja loja2 = new Loja("Outra Loja", "outra@email.com", "senha456",
                "98765432101", "Rua Outra, 456");
        service.cadastrar(loja2);

        List<Loja> resultado1 = service.buscarPorNome("loja");
        List<Loja> resultado2 = service.buscarPorNome("LOJA");
        List<Loja> resultado3 = service.buscarPorNome("Outra");

        assertEquals(2, resultado1.size());
        assertEquals(2, resultado2.size());
        assertEquals(1, resultado3.size());
        assertEquals("Outra Loja", resultado3.get(0).getNome());
    }

    @Test
    void testListarLojas() {
        service.cadastrar(loja);

        Loja loja2 = new Loja("Outra Loja", "outra@email.com", "senha456",
                "98765432101", "Rua Outra, 456");
        service.cadastrar(loja2);

        List<Loja> lojas = service.listarTodas();
        assertEquals(2, lojas.size());
    }

    @Test
    void testAtualizarLoja() {
        service.cadastrar(loja);

        loja.setNome("Nome Atualizado");
        loja.setEmail("novo@email.com");
        loja.setSenha("novaSenha123");
        loja.setEndereco("Novo Endereço");

        Loja atualizada = service.atualizar(loja);

        assertEquals("Nome Atualizado", atualizada.getNome());
        assertEquals("novo@email.com", atualizada.getEmail());
        assertEquals("novaSenha123", atualizada.getSenha());
        assertEquals("Novo Endereço", atualizada.getEndereco());
    }

    @Test
    void testAtualizarLojaInexistente() {
        Loja lojaInexistente = new Loja("Nome", "email@test.com", "senha", "cpf-inexistente", "endereco");
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(lojaInexistente)
        );
    }

    @Test
    void testRemoverLoja() {
        service.cadastrar(loja);

        assertTrue(service.buscarPorId(loja.getCpfCnpj()).isPresent());
        service.remover(loja.getCpfCnpj());
        assertFalse(service.buscarPorId(loja.getCpfCnpj()).isPresent());
    }

    @Test
    void testRemoverLojaInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.remover("cpf-inexistente")
        );
    }

    @Test
    void testCadastrarLojaJaExistente() {
        service.cadastrar(loja);

        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar(loja)
        );
    }

    @Test
    void testRemoverTodasAsLojas() {
        service.cadastrar(loja);
        Loja loja2 = new Loja("Outra Loja", "outra@email.com", "senha456",
                "98765432101", "Rua Outra, 456");
        service.cadastrar(loja2);

        assertEquals(2, service.listarTodas().size());

        service.removerLojas();

        assertTrue(service.listarTodas().isEmpty());
    }
}
