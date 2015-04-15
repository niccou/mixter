package mixter.domain.core.subscription.handlers;

import mixter.domain.EventPublisher;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.subscription.FollowerRepository;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.SubscriptionRepository;
import mixter.domain.identity.UserId;

import java.util.Set;

public class NotifyFollowerOfFolloweeMessage {

    private final FollowerRepository followerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EventPublisher eventPublisher;

    public NotifyFollowerOfFolloweeMessage(FollowerRepository followerRepository, SubscriptionRepository subscriptionRepository, EventPublisher eventPublisher) {

        this.followerRepository = followerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.eventPublisher = eventPublisher;
    }

    public void apply(MessagePublished messagePublished) {
        Set<UserId> followers = followerRepository.getFollowers(messagePublished.getAuthorId());
        for (UserId follower : followers) {
            Subscription subscription = subscriptionRepository.getById(new SubscriptionId(follower, messagePublished.getAuthorId()));
            subscription.notifyFollower(messagePublished.getMessageId(), eventPublisher);
        }
    }
}
