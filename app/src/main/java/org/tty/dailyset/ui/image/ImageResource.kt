package org.tty.dailyset.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.tty.dailyset.R

/**
 * a facade to visit the drawable resources.
 */
object ImageResource {
    @Composable
    fun user(): Painter {
        return painterResource(id = R.drawable.ic_user_24)
    }

    @Composable
    fun cell(): Painter {
        return painterResource(id = R.drawable.ic_cell_24)
    }

    @Composable
    fun table(): Painter {
        return painterResource(id = R.drawable.ic_table_24)
    }

    @Composable
    fun scan(): Painter {
        return painterResource(id = R.drawable.ic_scan_24)
    }

    @Composable
    fun set_auto(): Painter {
        return painterResource(id = R.drawable.ic_set_auto_24)
    }

    @Composable
    fun set_star(): Painter {
        return painterResource(id = R.drawable.ic_set_star_24)

    }

    @Composable
    fun add(): Painter {
        return painterResource(id = R.drawable.ic_add_24)
    }


    @Composable
    fun timelapse():Painter {
        return painterResource(id = R.drawable.ic_baseline_timelapse_24)
    }

    @Composable
    fun set(): Painter {
        return painterResource(id = R.drawable.ic_baseline_sort_24)
    }

    @Composable
    fun set_bird(): Painter {
        return painterResource(id = R.drawable.ic_set_bird_24)
    }

    @Composable
    fun set_book(): Painter {
        return painterResource(id = R.drawable.ic_set_book_24)
    }

    @Composable
    fun set_cat(): Painter {
        return painterResource(id = R.drawable.ic_set_cat_24)
    }

    @Composable
    fun set_cow(): Painter {
        return painterResource(id = R.drawable.ic_set_cow_24)
    }

    @Composable
    fun set_dog(): Painter {
        return painterResource(id = R.drawable.ic_set_dog_24)
    }

    @Composable
    fun set_duck(): Painter {
        return painterResource(id = R.drawable.ic_set_duck_24)
    }

    @Composable
    fun set_fire(): Painter {
        return painterResource(id = R.drawable.ic_set_fire_24)
    }

    @Composable
    fun set_frog(): Painter {
        return painterResource(id = R.drawable.ic_set_frog_24)
    }

    @Composable
    fun set_idea(): Painter {
        return painterResource(id = R.drawable.ic_set_idea_24)
    }

    @Composable
    fun set_leaf(): Painter {
        return painterResource(id = R.drawable.ic_set_leaf_24)
    }

    @Composable
    fun set_pin(): Painter {
        return painterResource(id = R.drawable.ic_set_pin_24)
    }

    @Composable
    fun set_plane(): Painter {
        return painterResource(id = R.drawable.ic_set_plane_24)
    }

    @Composable
    fun set_running(): Painter {
        return painterResource(id = R.drawable.ic_set_running_24)
    }

    @Composable
    fun set_sea(): Painter {
        return painterResource(id = R.drawable.ic_set_sea_24)
    }

    @Composable
    fun set_tutorial(): Painter {
        return painterResource(id = R.drawable.ic_set_tutorial_24)
    }

    @Composable
    fun set_vacation(): Painter {
        return painterResource(id = R.drawable.ic_set_vacation_24)
    }

    @Composable
    fun set_water(): Painter {
        return painterResource(id = R.drawable.ic_set_water_24)
    }

    @Composable
    fun set_wind(): Painter {
        return painterResource(id = R.drawable.ic_set_wind_24)
    }

    @Composable
    fun set_duration(): Painter {
        return painterResource(id = R.drawable.ic_set_duration_24)
    }
}