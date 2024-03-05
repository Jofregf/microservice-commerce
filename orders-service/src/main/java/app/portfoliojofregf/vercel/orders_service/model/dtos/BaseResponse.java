package app.portfoliojofregf.vercel.orders_service.model.dtos;

public record BaseResponse(String[] errorMessages) {

    public Boolean hasErrors() {
        return errorMessages != null && errorMessages.length > 0;
    }
}
