package com.techchallenge.encomendas.domain.enums;

public enum Status {
    RECEBIDA("Recebida"),
    RETIRADA("Retirada"),
    ENTREGUE("Entregue"),
    EM_TRANSITO("Em trânsito"),
    CANCELADO("Cancelado"),
    NOTIFICADA("Notificada"),
    NOTIFICACAO_CONFIRMADA("Notificação Confirmada");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
