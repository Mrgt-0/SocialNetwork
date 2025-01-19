package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByCommunity(Community community);
}
