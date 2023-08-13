package com.example.whatsapp.data

import android.location.Location
import com.example.whatsapp.cluster.ZoneClusterItem

data class MapState(val lastKnownLocation: Location?, val clusterItems: List<ZoneClusterItem>)

