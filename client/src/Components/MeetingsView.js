import React, { useState, useEffect } from "react";
import DateRangePicker from "@wojtekmaj/react-daterange-picker";

function MeetingsView(props) {
  const url = "http://127.0.0.1:8080";
  var selectedDepartmentId;
  var selectedEmployeeId;
  var inputTheme;

  const [receivedMeetings, setReceivedMeetings] = useState([]);
  const [receivedEmployees, setReceivedEmployees] = useState([]);
  const [receivedDepartments, setReceivedDepartments] = useState([]);

  const [dateRange, setDateRange] = useState();

  useEffect(() => {
    getDepartmentsQuery();
    getMeetingsQuery();
    getEmployeesQuery();
  }, []);

  const getMeetingsQuery = () => {
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/meetings", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        setReceivedMeetings(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getDepartmentsQuery = (event) => {
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/departments", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        setReceivedDepartments(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getEmployeesQuery = (event) => {
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/employees", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        setReceivedEmployees(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const searchQuery = (event) => {
    event.preventDefault();
    inputTheme = document.getElementById("theme-input").value;
    selectedEmployeeId = getIdFromSelect("empl-select");
    selectedDepartmentId = getIdFromSelect("dep-select");

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    const searchParams = new URLSearchParams();
    if (inputTheme !== null) {
      searchParams.append("theme", inputTheme);
    }
    if (typeof dateRange != "undefined" && dateRange !== null) {
      searchParams.append("dateFrom", formatDate(dateRange[0]));
      searchParams.append("dateTo", formatDate(dateRange[1]));
    }
    if (selectedDepartmentId !== null) {
      searchParams.append(
        "departmentId",
        Number.parseInt(selectedDepartmentId)
      );
    }
    if (selectedEmployeeId !== null) {
      searchParams.append("employeeId", Number.parseInt(selectedEmployeeId));
    }

    fetch(url + "/search?" + searchParams.toString(), requestOptions)
      .then((response) => {
        return response.json();
      })
      .then((response) => setReceivedMeetings(response))
      .catch((err) => console.log("Error " + err));
  };

  function getIdFromSelect(selectId) {
    let select = document.getElementById(selectId);
    const selectedIndex = select.selectedIndex;
    return select.options[selectedIndex].getAttribute("id-key");
  }

  function formatDate(date) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear(),
      hour = "" + d.getHours(),
      minute = "" + d.getMinutes();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;
    if (hour.length < 2) hour = "0" + hour;
    if (minute.length < 2) minute = "0" + minute;

    return [year, month, day].join("-") + " " + ["00", "00"].join(":");
  }

  return (
    <form class="flexbox-vertical">
      <div class="flexbox-horizontal">
        <div class="flexbox-vertical">
          <label class="flexbox-horizontal">
            <label class="bold-text">Тема</label>
            <input id="theme-input" type="text" />
          </label>

          <label class="flexbox-horizontal">
            <label class="bold-text">Время проведения: </label>
            <div>
              <DateRangePicker onChange={setDateRange} value={dateRange} />
            </div>
          </label>
        </div>

        <div class="flexbox-vertical">
          <label class="flexbox-horizontal">
            <label class="bold-text">Подразделение</label>
            <select id="dep-select">
              <option value="none" selected>
                Подразделение
              </option>
              {receivedDepartments.map((department) => {
                return (
                  <option id-key={department.id}> {department.name}</option>
                );
              })}
            </select>
          </label>

          <label class="flexbox-horizontal">
            <label class="bold-text">С участием</label>
            <select id="empl-select">
              <option value="none" selected>
                Сотрудник
              </option>
              {receivedEmployees.map((employee) => {
                return (
                  <option id-key={employee.id}> {employee.shortName}</option>
                );
              })}
            </select>
          </label>
        </div>
      </div>

      <label>
        <button onClick={searchQuery}>Применить фильтр</button>
        <div class="button-divider" />
        <button>Показать все</button>
      </label>

      <div>
        <table id="meetingTable">
          <tbody>
            <tr>
              <th>Время</th>
              <th>Тема</th>
              <th>Подразделение</th>
              <th>Ответственный</th>
              <th>Состав</th>
            </tr>
            {receivedMeetings.map((meeting) => {
              return (
                <tr key={meeting.id}>
                  <td>{meeting.time}</td>
                  <td
                    class="link"
                    onClick={() => props.DisplayMeeting(meeting.id)}
                  >
                    {meeting.theme}
                  </td>
                  <td>{meeting.department.name}</td>
                  <td>{meeting.organizer.shortName}</td>
                  <td>{meeting.count}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <label>
        <button onClick={() => props.DisplayMeeting()}>Добавить новое</button>
      </label>
    </form>
  );
}
export default MeetingsView;
