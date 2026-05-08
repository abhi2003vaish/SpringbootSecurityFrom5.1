package com.codingshuttle.SecurityApp.SecurityApplication.utils;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.PostDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.PostEntity;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwnerOfPost(long postId){
//        taking current user
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        taking data from the database  for the specified postId
        PostDTO post = postService.getPostById(postId);
//        comparing the user id of the current user with the user id of the author of the post
        return post.getAuthor().getId().equals(user.getId());

    }
}
