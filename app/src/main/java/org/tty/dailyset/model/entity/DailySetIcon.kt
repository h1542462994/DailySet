package org.tty.dailyset.model.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.tty.dailyset.ui.image.ImageResource

enum class DailySetIcon(val key: String) {
    Bird("bird"),
    Book("book"),
    Cat("cat"),
    Cow("cow"),
    Dog("dog"),
    Duck("duck"),
    Fire("fire"),
    Frog("frog"),
    Idea("idea"),
    Leaf("leaf"),
    Pin("pin"),
    Plane("plane"),
    Running("running"),
    Sea("sea"),
    Tutorial("tutorial"),
    Vacation("vacation"),
    Water("water"),
    Wind("wind")


}

@Composable
fun DailySetIcon?.toImageResource(): Painter {
    if (this == null) {
        return ImageResource.list()
    }
    return when (this) {
        DailySetIcon.Bird -> ImageResource.set_bird()
        DailySetIcon.Book -> ImageResource.set_book()
        DailySetIcon.Cat -> ImageResource.set_cat()
        DailySetIcon.Cow -> ImageResource.set_cow()
        DailySetIcon.Dog -> ImageResource.set_dog()
        DailySetIcon.Duck -> ImageResource.set_duck()
        DailySetIcon.Fire -> ImageResource.set_fire()
        DailySetIcon.Frog -> ImageResource.set_frog()
        DailySetIcon.Idea -> ImageResource.set_idea()
        DailySetIcon.Leaf -> ImageResource.set_leaf()
        DailySetIcon.Pin -> ImageResource.set_pin()
        DailySetIcon.Plane -> ImageResource.set_plane()
        DailySetIcon.Running -> ImageResource.set_running()
        DailySetIcon.Sea -> ImageResource.set_sea()
        DailySetIcon.Tutorial -> ImageResource.set_tutorial()
        DailySetIcon.Vacation -> ImageResource.set_vacation()
        DailySetIcon.Water -> ImageResource.set_water()
        DailySetIcon.Wind -> ImageResource.set_wind()
    }
}