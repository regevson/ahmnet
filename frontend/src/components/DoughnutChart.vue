<template>
  <Doughnut
    :chart-options="chartOptions"
    :chart-data="chartData"
    :width="width"
    :height="height"
  />
</template>

<script>
import { Doughnut } from 'vue-chartjs/legacy'
import {Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale)

export default {
  name: 'DoughnutChart',
  components: {Doughnut},

  props: {
    labels: {
      type: Array,
      default: () => []
    },
    values: {
      type: Array,
      default: () => []
    },
    width: {
      type: Number,
      default: 200
    },
    height: {
      type: Number,
      default: 200
    },
  },

  data() {
    return {
      chartData: {
        datasets: [{backgroundColor: ['#499082', '#1B2730', '#bf8a4a', '#476a91', '#c28b8b']}]
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },

  created() {
    this.chartData.labels = this.labels;
    this.chartData.datasets[0].data = this.values;
  },

}
</script>

