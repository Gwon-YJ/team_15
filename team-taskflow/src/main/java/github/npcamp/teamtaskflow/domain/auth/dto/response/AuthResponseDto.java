package github.npcamp.teamtaskflow.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import github.npcamp.teamtaskflow.domain.common.base.Identifiable;
import github.npcamp.teamtaskflow.domain.user.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto implements Identifiable{

    private String token;
    private UserResponseDto user;

    @Override
    @JsonIgnore
    public Long getId() {
        return user != null ? user.getId() : null;
    }
}
