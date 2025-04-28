package com.techchallenge.encomendas.adapters.in;

import com.techchallenge.encomendas.application.dto.MoradorDTO;
import com.techchallenge.encomendas.application.usecases.morador.BuscarMoradorUseCase;
import com.techchallenge.encomendas.application.usecases.morador.CadastrarMoradorUseCase;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moradores")
public class MoradorController {

    private final CadastrarMoradorUseCase cadastrarMoradorUseCase;
    private final BuscarMoradorUseCase buscarMoradorUseCase;

    public MoradorController(CadastrarMoradorUseCase cadastrarMoradorUseCase,
                             BuscarMoradorUseCase buscarMoradorUseCase) {
        this.cadastrarMoradorUseCase = cadastrarMoradorUseCase;
        this.buscarMoradorUseCase = buscarMoradorUseCase;
    }

    @PostMapping
    public ResponseEntity<MoradorDTO> cadastrarMorador(@RequestBody MoradorDTO moradorDTO) {
        var novoMorador = cadastrarMoradorUseCase.cadastrar(moradorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMorador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoradorDTO> buscarPorId(@PathVariable Long id) {
        var morador = buscarMoradorUseCase.buscarPorId(id);
        return ResponseEntity.ok(morador);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<MoradorDTO> buscarPorCpf(@RequestParam String cpf) {
        var morador = buscarMoradorUseCase.buscarPorCpf(cpf);
        return ResponseEntity.ok(morador);
    }

    @GetMapping(params = "apartamento")
    public ResponseEntity<MoradorDTO> buscarPorApartamento(@RequestParam String apartamento) {
        var moradores = buscarMoradorUseCase.buscarPorApartamento(apartamento);
        return ResponseEntity.ok(moradores);
    }

    @GetMapping
    public ResponseEntity<List<MoradorDTO>> listarTodosMoradores() {
        var moradores = buscarMoradorUseCase.listarTodosMoradores();
        if (moradores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(moradores);
    }
}
