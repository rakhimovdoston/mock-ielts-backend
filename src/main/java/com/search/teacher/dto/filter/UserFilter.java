package com.search.teacher.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.search.teacher.model.enums.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ApiModel(description = "User entity parameters")
@ToString(onlyExplicitlyIncluded = true, doNotUseGetters = true)
public class UserFilter extends PageFilter {

    @ApiModelProperty(value = "The Search Key filter")
    @ToString.Include
    private String search = "";

    @ApiModelProperty(value = "roleType")
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public String getSearchForQuery() {
        return StringUtils.isNotEmpty(search) ? "%" + search.toLowerCase().replace("_", "\\_") + "%" : search;
    }


}
