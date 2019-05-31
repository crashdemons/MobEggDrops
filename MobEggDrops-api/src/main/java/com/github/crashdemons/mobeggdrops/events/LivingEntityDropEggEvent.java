/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.github.crashdemons.mobeggdrops.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event created by the PlayerHeads plugin when a [living] entity is beheaded.
 *
 * This class will usually be instanced as either MobDropHeadEvent (for mobs) or
 * PlayerDropHeadEvent (for a Player).
 *
 * Cancellable.
 *
 * <i>Note:</i> Some of this documentation was inferred after the fact and may
 * be inaccurate.
 *
 * @author meiskam
 */
public class LivingEntityDropEggEvent extends EntityEvent implements Cancellable, DropEggEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean canceled = false;
    private final ItemStack itemDrop;

    /**
     * Construct the event
     *
     * @param entity the [living] entity droping the head
     * @param drop the head item being dropped
     */
    LivingEntityDropEggEvent(final LivingEntity entity, final ItemStack drop) {
        super(entity);
        this.itemDrop = drop;
    }

    /**
     * Gets the item that will drop from the beheading.
     *
     * @return mutable ItemStack that will drop into the world once this event
     * is over
     */
    @SuppressWarnings("unused")
    @Override
    public ItemStack getDrop() {
        return itemDrop;
    }

    /**
     * Gets the entity that was beheaded
     *
     * @return the beheaded entity
     */
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Whether the event has been cancelled.
     *
     * @return Whether the event has been cancelled.
     */
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancel whether the event should be cancelled.
     */
    @Override
    public void setCancelled(final boolean cancel) {
        canceled = cancel;
    }

    /**
     * Get a list of handlers for the event.
     *
     * @return a list of handlers for the event
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Get a list of handlers for the event.
     *
     * @return a list of handlers for the event
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
