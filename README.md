# CustomBaubles
CustomBaubles

displayName

Renderer:
inventory:
RenderItem.renderItemAndEffectIntoGui

1st-person-view:
EntityRenderer.renderHand() which calls ItemRenderer.renderItemInFirstPerson()

3rd-person-view:
RenderPlayer.renderPlayer(), which calls RenderPlayer.renderSpecials()

drop item:
RenderItem.doRenderItem()
