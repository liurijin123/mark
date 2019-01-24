## 加载OSM
```
new ol.layer.Tile({
  projection: 'EPSG:3857',
  source: new ol.source.OSM()
})
```
## 加载切片地图
```
new ol.layer.Tile({
  title: "Global Imagery",
  source: new ol.source.TileWMS({
    url: host + 'geoserver/anjiSYS/wms',
    params: {LAYERS: 'shenghui', VERSION: '1.1.1'}
  })
})
```
## 加载WFS
```
new ol.layer.Vector({
  title: "Global Imagery",
  source: new ol.source.Vector({
    format: new ol.format.GeoJSON({
      geometryName: 'the_geom'
    }),
    url: host + 'geoserver/anjiSYS/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=anjiSYS:shenghui&maxFeatures=50&outputFormat=application/json'
  }),
  style: new ol.style.Style({
    image: new ol.style.Circle({
      radius: 3,
      fill: new ol.style.Fill({color: 'red'})
    })
  })
})
```
## 加载GeoJson
```
new ol.layer.Vector({
  title: 'Earthquakes',
  projection: 'EPSG:3857',
  source: new ol.source.Vector({
     url:"${pageContext.request.contextPath}/data/shenghuiJSONS.geojson",
     format: new ol.format.GeoJSON()
  }),
  style: new ol.style.Style({
      image: new ol.style.Circle({
        radius: 3,
        fill: new ol.style.Fill({color: 'red'})
      })
  })
})
```
## getPixelFromCoordinate返回null
```
/**
         * 经纬度转换为屏幕像素
         *
         * @param {Array.<number>} geoCoord  经纬度
         * @return {Array.<number>}
         * @public
         */
        self.geoCoord2Pixel = function (geoCoord) {
            var fromLonLat = ol.proj.fromLonLat(geoCoord);
            //alert(fromLonLat);
            var Pixe = self._map.getPixelFromCoordinate(fromLonLat);
            //alert(Pixe);
            return Pixe;
        };
```

I'd be careful with this. You might get wrong results, e.g. when the map does not have the final layout yet. It is better to wait with the first coordinate to pixel conversion until the map is rendered. You do not need a timeout for this, we have the 'postrender' event on ol.Map. So in your initialisation code, you could do something like this:
```
map.once('postrender', function() {
  // safe to call map.getPixelFromCoordinate from now on
});
```




























