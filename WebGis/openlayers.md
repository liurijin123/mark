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






























