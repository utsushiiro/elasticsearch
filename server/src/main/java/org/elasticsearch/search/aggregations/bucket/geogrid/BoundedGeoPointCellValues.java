/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.aggregations.bucket.geogrid;

import org.elasticsearch.common.geo.GeoBoundingBox;
import org.elasticsearch.index.fielddata.MultiGeoValues;

/**
 * Class representing {@link CellValues} whose values are filtered
 * according to whether they are within the specified {@link GeoBoundingBox}.
 *
 * The specified bounding box is assumed to be bounded.
 */
class BoundedGeoPointCellValues extends CellValues {

    private final GeoBoundingBox geoBoundingBox;

    protected BoundedGeoPointCellValues(MultiGeoValues geoValues, int precision, GeoGridTiler tiler, GeoBoundingBox geoBoundingBox) {
        super(geoValues, precision, tiler);
        this.geoBoundingBox = geoBoundingBox;
    }


    @Override
    int advanceValue(MultiGeoValues.GeoValue target, int valuesIdx) {
        if (geoBoundingBox.pointInBounds(target.lon(), target.lat())) {
            values[valuesIdx] = tiler.encode(target.lon(), target.lat(), precision);
            return valuesIdx + 1;
        }
        return valuesIdx;
    }
}