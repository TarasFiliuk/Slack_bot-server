package com.mybot.infrastructure.domain.git;

import com.mybot.infrastructure.domain.git.nested.Git;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitInfo {
    private Git git;
    private ArrayList<String> activeProfiles;
}
