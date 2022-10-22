using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Appointment
    {
        public static AppointmentDTO ToDTO(Appointment item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new AppointmentDTO
            {
                DateTimeStart = item.DateTimeStart,
                DateTimeEnd = item.DateTimeEnd,
                Shop = singleLevel ? null : Shop.ToDTO(item.Shop),
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                ShopId = item.ShopId
            };
        }

        public static ICollection<AppointmentDTO> ToListDTO(ICollection<Appointment> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public AppointmentDTO ToDTO(bool singleLevel = true)
        {
            return new AppointmentDTO
            {
                DateTimeStart = DateTimeStart,
                DateTimeEnd = DateTimeEnd,
                Shop = singleLevel ? null : Shop.ToDTO(Shop),
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                ShopId = ShopId
            };
        }
    }

}