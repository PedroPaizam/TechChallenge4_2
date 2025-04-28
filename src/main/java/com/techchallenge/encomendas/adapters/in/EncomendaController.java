package com.techchallenge.encomendas.adapters.in;

import com.techchallenge.encomendas.application.dto.EncomendaDTO;
import com.techchallenge.encomendas.application.dto.NovaEncomendaDTO;
import com.techchallenge.encomendas.application.usecases.encomenda.BuscarEncomendaUseCase;
import com.techchallenge.encomendas.application.usecases.encomenda.RegistrarEncomendaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaController {

    private final RegistrarEncomendaUseCase registrarEncomendaUseCase;
    private final BuscarEncomendaUseCase buscarEncomendaUseCase;

    public EncomendaController(RegistrarEncomendaUseCase registrarEncomendaUseCase,
                               BuscarEncomendaUseCase buscarEncomendaUseCase) {
        this.registrarEncomendaUseCase = registrarEncomendaUseCase;
        this.buscarEncomendaUseCase = buscarEncomendaUseCase;
    }

    @PostMapping
    public ResponseEntity<EncomendaDTO> registrarEncomenda(@RequestBody NovaEncomendaDTO novaEncomendaDTO) {
        var encomendaDTO = registrarEncomendaUseCase.registrar(novaEncomendaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(encomendaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncomendaDTO> buscarPorId(@PathVariable Long id) {
        var encomenda = buscarEncomendaUseCase.buscarPorId(id);
        return ResponseEntity.ok(encomenda);
    }

    @GetMapping("/morador/{moradorId}")
    public ResponseEntity<List<EncomendaDTO>> buscarPorMorador(@PathVariable Long moradorId) {
        var encomendas = buscarEncomendaUseCase.buscarPorMorador(moradorId);
        return ResponseEntity.ok(encomendas);
    }

    @PutMapping("/{id}/retirada")
    public ResponseEntity<EncomendaDTO> registrarRetirada(@PathVariable Long id) {
        var encomenda = registrarEncomendaUseCase.registrarRetirada(id);
        return ResponseEntity.ok(encomenda);
    }

    @PutMapping("/{id}/confirmar-notificacao")
    public ResponseEntity<EncomendaDTO> confirmarNotificacao(@PathVariable Long id) {
        var sucesso = registrarEncomendaUseCase.confirmarNotificacao(id);

        if (!sucesso) {
            throw new IllegalStateException("Não foi possível confirmar a notificação para a encomenda com ID: " + id);
        }

        var encomenda = buscarEncomendaUseCase.buscarPorId(id);
        return ResponseEntity.ok(encomenda);
    }
}
