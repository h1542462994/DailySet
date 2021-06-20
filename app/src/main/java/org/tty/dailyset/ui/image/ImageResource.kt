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


}