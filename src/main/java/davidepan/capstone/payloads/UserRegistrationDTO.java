package davidepan.capstone.payloads;

public record UserRegistrationDTO(
        String name,
        String surname,
        String email,
        String password
) {
}
