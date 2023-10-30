package com.socialmedia.api.core.seeder;

import com.github.javafaker.Faker;
import com.socialmedia.api.model.entity.*;
import com.socialmedia.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        initializeFakeData();
        alreadySetup = true;
    }

    private void initializeFakeData() {
        String myEmail = "zraelwalker@gmail.com";
        if (userRepository.existsByEmail(myEmail)) {
            return;
        }

        Faker faker = Faker.instance(Locale.getDefault());

        //create users
        int userCount = 10;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            User user = User.builder()
                    .email(faker.name().firstName() + (i + 1) + "@example.com")
                    .username(faker.name().username() + (i + 1))
                    .password(passwordEncoder.encode("$Password123"))
                    .build();

            if (users.stream().noneMatch(persistedUser -> persistedUser.getEmail().equals(myEmail))) {
                user.setEmail(myEmail);
                user.setUsername("zrael");
            }

            users.add(userRepository.save(user));
        }

        //create posts per user
        int postCountPerUser = faker.random().nextInt(3, 8);
        Set<Post> posts = new LinkedHashSet<>();
        List<User> usersWithPost = new ArrayList<>();
        for (User user : users) {
            for (int i = 0; i < postCountPerUser; i++) {
                Post post = Post.builder()
                        .content(faker.lorem().sentence(8))
                        .user(user)
                        .build();

                Post persistedPost = postRepository.save(post);
                posts.add(persistedPost);
                user.addPost(persistedPost);
            }

            usersWithPost.add(userRepository.save(user));
        }

        //create comments and likes from each user per post
        Set<Comment> comments = new LinkedHashSet<>();
        for (User user : usersWithPost) {
            for (Post post : posts) {
                Comment comment = Comment.builder()
                        .content(faker.lorem().sentence(8))
                        .post(post)
                        .user(user)
                        .build();

                Comment persistedComment = commentRepository.save(comment);
                comments.add(persistedComment);
                post.addComment(persistedComment);

                PostLike postLike = PostLike.builder()
                        .post(post)
                        .user(user)
                        .build();

                PostLike like = postLikeRepository.save(postLike);
                post.addLike(like);
                postRepository.save(post);
            }
        }

        //create likes from each user per comment
        for (User user : usersWithPost) {
            for (Comment comment : comments) {
                CommentLike commentLike = CommentLike.builder()
                        .comment(comment)
                        .user(user)
                        .build();

                CommentLike like = commentLikeRepository.save(commentLike);
                comment.addLike(like);
                commentRepository.save(comment);
            }
        }

        //update following per user
        for (User userWithPost : usersWithPost) {
            LinkedHashSet<User> otherUsers = users.stream()
                    .filter(user -> !user.equals(userWithPost))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            for (User otherUser : otherUsers) {
                userWithPost.addFollower(otherUser);
                userRepository.save(userWithPost);

                otherUser.addFollowing(userWithPost);
                userRepository.save(otherUser);
            }
        }

    }


}
