package org.example.socialnetwork.Repository;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByCommunity(Community community);
    @Modifying
    @Transactional
    @Query("DELETE FROM CommunityMember cm WHERE cm.community.id = :communityId")
    void deleteByCommunityId(@Param("communityId") Long communityId);
}
