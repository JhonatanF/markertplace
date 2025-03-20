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
    void testRemoverLoja() {
        service.cadastrar(loja);
        
        assertTrue(service.buscarPorId(loja.getCpfCnpj()).isPresent());
        service.remover(loja.getCpfCnpj());
        assertFalse(service.buscarPorId(loja.getCpfCnpj()).isPresent());
    }

    @Test
    void testAtualizarLojaInexistente() {
        Loja lojaInexistente = new Loja("Nome", "email@test.com", "senha", "cpf-inexistente", "endereco");
        assertThrows(IllegalArgumentException.class, () -> 
            service.atualizar(lojaInexistente)
        );
    }

    @Test
    void testCadastrarLojaJaExistente() {
        service.cadastrar(loja.getNome(), loja.getEmail(), loja.getSenha(), 
                        loja.getCpfCnpj(), loja.getEndereco());
        
        assertThrows(IllegalArgumentException.class, () ->
            service.cadastrar(loja.getNome(), loja.getEmail(), loja.getSenha(), 
                           loja.getCpfCnpj(), loja.getEndereco())
        );
    }
}
